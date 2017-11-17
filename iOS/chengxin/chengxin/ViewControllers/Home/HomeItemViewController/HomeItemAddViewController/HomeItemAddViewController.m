//
//  HomeItemAddViewController.m
//  chengxin
//
//  Created by seniorcoder on 11/1/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "HomeItemAddViewController.h"
#import "ChoiceCategoryViewController.h"
#import "Global.h"

@interface HomeItemAddViewController ()<ChoiceCategoryViewDelegate>
{
    NSArray *cityArray, *otherArray;
    ChoiceCategoryViewController *choiceCategoryVC;
    NSDictionary *selectedCategoryDic;
    NSString *cityID, *cityOtherID;
    ImageChooseViewController *logoPicker;
    NSInteger cityRow, cityOtherRow;
}
@end

@implementation HomeItemAddViewController
@synthesize cityPickerView, citySelectView;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    cityRow = 0;
    cityOtherRow = 0;
    if([CommonData sharedInstance].addItemServiceIndex == ITEM_PAGE) {
        self.cityNoSelectView.hidden = YES;
        cityArray  = [[NSArray alloc] init];
        [self getCityFromeServer];
    }else{
        self.cityNoSelectView.hidden = NO;
    }

}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)getCityFromeServer {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    [dicParams setObject:@"getCityList" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    [[WebAPI sharedInstance] sendPostRequest:ACTION_GETCITYLIST Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                cityArray = (NSMutableArray *)(dicRes[@"data"]);
                [self.cityPickerView reloadAllComponents];
            }
        }
    }];
}

- (IBAction)onAdd:(id)sender {
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    if([CommonData sharedInstance].addItemServiceIndex == ITEM_PAGE) {
        [dicParams setObject:@"addItem" forKey:@"pAct"];
        [dicParams setObject:self.changedItemID forKey:@"itemId"];
        [dicParams setObject:cityOtherID forKey:@"cityId"];
    }else{
        [dicParams setObject:@"addService" forKey:@"pAct"];
        [dicParams setObject:self.changedItemID forKey:@"serviceId"];
    }

    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    if(self.logoImageLabel.isHidden) {
        NSData *imageData = UIImagePNGRepresentation(self.logoImageView.image);
        [dicParams setObject:imageData forKey:@"logo"];
    }
    [dicParams setObject:self.nameTextField.text forKey:@"name"];
    [dicParams setObject:selectedCategoryDic[@"id"] forKey:@"fenleiId"];
    [dicParams setObject:self.itemCommentTextView.text forKey:@"comment"];
    [dicParams setObject:self.needTextField.text forKey:@"need"];
    [dicParams setObject:self.weburlTextField.text forKey:@"weburl"];
    [dicParams setObject:self.isShowButton.selected? @"1" : @"0" forKey:@"isShow"];
    [dicParams setObject:self.contactNameTextField.text forKey:@"contactName"];
    [dicParams setObject:self.contactMobileTextField.text forKey:@"contactMobile"];
    [dicParams setObject:self.contactWeixinTextField.text forKey:@"contactWeixin"];
    [[WebAPI sharedInstance] sendPostRequest:ACTION_ADDITEM Parameters:dicParams :^(NSObject *resObj) {
        
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
            }
        }
    }];
}

- (void)showPickerView {
    if(citySelectView.isHidden) {
        [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        citySelectView.hidden = NO;
        [UIView animateWithDuration:1.0f animations:^{
            [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height - citySelectView.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        } completion:^(BOOL finished) {
        }];
    }else{
        [UIView animateWithDuration:1.0f animations:^{
            [citySelectView setFrame:CGRectMake(0, self.view.frame.size.height, citySelectView.frame.size.width, citySelectView.frame.size.height)];
        } completion:^(BOOL finished) {
            citySelectView.hidden = YES;
        }];
    }
}
#pragma mark - IBAction
- (IBAction)onBackAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)showPickerAction:(id)sender {
    [self showPickerView];
}

- (IBAction)onCancelAction:(id)sender {
    [self showPickerView];
}

- (IBAction)onSelectCityAction:(id)sender {
    [self showPickerView];
    NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:cityRow];
    NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
    NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[cityOtherRow]);
    cityOtherID = cityOtherDic[@"id"];
    self.cityLabel.text = [NSString stringWithFormat:@"%@ , %@", cityDic[@"name"], cityOtherDic[@"name"]];
}

- (IBAction)onChoiceKindAction:(id)sender {
//    if(!choiceCategoryVC) {
        choiceCategoryVC = [[ChoiceCategoryViewController alloc] initWithNibName:@"ChoiceCategoryViewController" bundle:nil];
        choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width, 64.f, self.view.frame.size.width - 44, self.view.frame.size.height - 64.f);
        choiceCategoryVC.delegate = self;
    if([CommonData sharedInstance].addItemServiceIndex == ITEM_PAGE) {
        choiceCategoryVC.titleLabel.text = @"项目分类：";
        choiceCategoryVC.categoryType = ITEM_CATEGORY;
    }else{
        choiceCategoryVC.titleLabel.text = @"服务分类：";
        choiceCategoryVC.categoryType = SERVICE_CATEGORY;
    }
        choiceCategoryVC.view.hidden = YES;
//    }
//    if(choiceCategoryVC.view.hidden) {
        choiceCategoryVC.selectedCategory = selectedCategoryDic;
        [choiceCategoryVC getData];
        choiceCategoryVC.view.hidden = NO;
        [self.view addSubview:choiceCategoryVC.view];
        [UIView animateWithDuration:1.0f animations:^{
            choiceCategoryVC.view.frame = CGRectMake(self.view.frame.size.width - choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.origin.y, choiceCategoryVC.view.frame.size.width, choiceCategoryVC.view.frame.size.height);
        } completion:^(BOOL finished) {
            self.transView.hidden = NO;
        }];
//    }
}

- (IBAction)onShowAction:(id)sender {
    self.isShowButton.selected = !self.isShowButton.selected;
}

- (IBAction)onSlideCategoryView:(UISwipeGestureRecognizer *)gestureRecognizer {
    [self hideChoiceCategoryView:nil];
}

-(IBAction)onLogo:(UITapGestureRecognizer*)recognizer {
    logoPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    logoPicker.delegate = self;
    logoPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    [self.navigationController presentViewController:logoPicker animated:YES completion:nil];
    
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

#pragma mark - UIPickerViewDelegate, UIPickerViewDataSource

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 2;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if(component == 0) {
        return cityArray.count;
    }else{
        if(cityArray.count > 0) {
            NSDictionary *cityOtherDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
            NSArray *cityOtherArray = (NSArray *)(cityOtherDic[@"cities"]);
            return cityOtherArray.count;
        }else{
            return 0;
        }
    }
}


- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    if(component == 0) {
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:row];
        return cityDic[@"name"];
    }
    else {
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
        NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
        NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[row]);
        return cityOtherDic[@"name"];
    }
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if(component == 0) {
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:row];
        cityID = cityDic[@"id"];
        cityRow = row;
        [self.cityPickerView reloadAllComponents];
    }
    else {
        NSDictionary *cityDic = (NSDictionary *)[cityArray objectAtIndex:[pickerView selectedRowInComponent:0]];
        NSArray *cityOtherArray = (NSArray *)(cityDic[@"cities"]);
        NSDictionary *cityOtherDic = (NSDictionary *)(cityOtherArray[row]);
        cityOtherID = cityOtherDic[@"id"];
        cityOtherRow = row;
        self.cityLabel.text = [NSString stringWithFormat:@"%@ , %@", cityDic[@"name"], cityOtherDic[@"name"]];
    }
}

#pragma mark - UITextViewDelegate
- (void)textViewDidChange:(UITextView *)textView {
    NSInteger textLength = [self.itemCommentTextView.text length];
    if(textLength <= 300) {
        self.itemCommentLimitLabel.text = [NSString stringWithFormat:@"%d/300", (int)textLength];
    }
}
- (BOOL)textView:(UITextView *)iTextView shouldChangeTextInRange:(NSRange)iRange replacementText:(NSString *)iText {
    if (iText.length < 300) {
        return YES;
    }
    iTextView.text = [iText substringToIndex:300];
    return NO;
}

#pragma mark - ImageChooseViewController
-(void)chooseViewController:(ImageChooseViewController *)vc shownImage:(UIImage *)image
{
    if(vc == logoPicker && image) {
        [self.logoImageView setImage:image];
        self.logoImageLabel.hidden = NO;
    }
    else
        self.logoImageLabel.hidden = YES;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
