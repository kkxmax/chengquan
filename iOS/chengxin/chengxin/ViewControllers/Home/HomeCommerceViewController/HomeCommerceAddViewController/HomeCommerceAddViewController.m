//
//  HomeCommerceAddViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeCommerceAddViewController.h"
#import "ChoiceCategoryViewController.h"
#import "Global.h"

@interface HomeCommerceAddViewController ()<ChoiceCategoryViewDelegate>
{
    ChoiceCategoryViewController *choiceCategoryVC;
    NSDictionary *selectedCategoryDic;
    ImageChooseViewController *logoPicker;
}
@end

@implementation HomeCommerceAddViewController
@synthesize viewPicture, heightPicView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    
    NSMutableArray *aryPic = [[NSMutableArray alloc] init];
    CGRect rect = viewPicture.frame;
    [viewPicture setFrame:rect :aryPic];
    viewPicture.delegate = self;
    heightPicView.constant = viewPicture.frame.size.height;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onChoiceKindAction:(id)sender {
    choiceCategoryVC = [[ChoiceCategoryViewController alloc] initWithNibName:@"ChoiceCategoryViewController" bundle:nil];
    choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, 64.f, self.view.frame.size.width - 44, self.view.frame.size.height - 64.f);
    choiceCategoryVC.delegate = self;
    choiceCategoryVC.titleLabel.text = @"产品分类：";
    choiceCategoryVC.categoryType = PRODUCT_CATEGORY;
    choiceCategoryVC.pleixingID = @"";
    choiceCategoryVC.view.hidden = YES;
    choiceCategoryVC.selectedCategory = selectedCategoryDic;
    [choiceCategoryVC getData];
    choiceCategoryVC.view.hidden = NO;
    [self.view addSubview:choiceCategoryVC.view];
    [UIView animateWithDuration:1.0f animations:^{
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width - choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
    } completion:^(BOOL finished) {
        self.transView.hidden = NO;
    }];
}

- (IBAction)onAdd:(id)sender {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"addProduct" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    [dicParams setObject:self.changedProductID forKey:@"productId"];
    [dicParams setObject:self.nameTextField.text forKey:@"name"];
    [dicParams setObject:self.isMainTrueButton.selected? @"1" : @"0" forKey:@"isMain"];
    [dicParams setObject:self.priceTextField forKey:@"price"];
    [dicParams setObject:selectedCategoryDic[@"id"] forKey:@"pleixingId"];
    [dicParams setObject:self.commentTextView.text forKey:@"comment"];
    [dicParams setObject:self.weburlTextField.text forKey:@"weburl"];
    [dicParams setObject:self.addressTextField.text forKey:@"saleAddr"];
    NSMutableArray *imageArray = [NSMutableArray array];
    for(int i  = 0; i < viewPicture.aryPicture.count; i++) {
        UIImage *image = [viewPicture.aryPicture objectAtIndex:i];
        NSData *imageData = UIImagePNGRepresentation(image);
        [imageArray addObject:imageData];
    }
    [dicParams setObject:imageArray forKey:@"productImages"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_ADDPRODUCT Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
            }
        }
    }];
}

- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onMainAction:(id)sender {
    UIButton *button = (UIButton *)sender;
    if(self.isMainTrueButton == button) {
        if(self.isMainTrueButton.selected)
            return;
        self.isMainTrueButton.selected = YES;
        self.isMainFalseButton.selected = NO;
    }else{
        if(self.isMainFalseButton.selected)
            return;
        self.isMainFalseButton.selected = YES;
        self.isMainTrueButton.selected = NO;
    }
}

#pragma mark - ChoiceCategoryViewDelegate
- (void)hideChoiceCategoryView:(NSDictionary *)categoryDic {
    self.transView.hidden = YES;
    [UIView animateWithDuration:1.0f animations:^{
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
    } completion:^(BOOL finished) {
        choiceCategoryVC.view.hidden = YES;
        [choiceCategoryVC.view removeFromSuperview];
    }];
    if(categoryDic) {
        selectedCategoryDic = categoryDic;
        self.categoryLabel.text = categoryDic[@"title"];
    }
}

#pragma mark - UITextViewDelegate
- (void)textViewDidChange:(UITextView *)textView {
    NSInteger textLength = [self.commentTextView.text length];
    if(textLength <= 300) {
        self.productCommentLimitLabel.text = [NSString stringWithFormat:@"%d/300", (int)textLength];
    }
}
- (BOOL)textView:(UITextView *)iTextView shouldChangeTextInRange:(NSRange)iRange replacementText:(NSString *)iText {
    if (iText.length < 300) {
        return YES;
    }
    iTextView.text = [iText substringToIndex:300];
    return NO;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

-(void) changedPictureView:(NSMutableArray *)aryPicture :(int)height {
    heightPicView.constant = height;
}

@end
