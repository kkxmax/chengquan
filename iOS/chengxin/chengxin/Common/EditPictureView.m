//
//  EditPictureView.m
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "EditPictureView.h"
#import "Global.h"

@interface EditPictureView ()
{
    ImageChooseViewController *logoPicker;
}
@end

@implementation EditPictureView
@synthesize aryPicture, strAddPic;
-(id) initWithFrame:(CGRect)frame :(NSMutableArray*)pictures
{
    self = [super initWithFrame:frame];

    strAddPic = @"最多五张";
    aryPicture = pictures;

    if(self.maximumPictureCount == 5) {
        strAddPic = @"最多五张";
    }else if(self.maximumPictureCount == 6){
        strAddPic = @"最多六张";
    }
    
    [self reloadPictureData];
    
    return self;
}

-(void) setFrame:(CGRect)frame :(NSMutableArray*)pictures {
    
    [self setFrame:frame];
    
    aryPicture = pictures;
    strAddPic = @"最多五张";
    if(self.maximumPictureCount == 5) {
        strAddPic = @"最多五张";
    }else if(self.maximumPictureCount == 6){
        strAddPic = @"最多六张";
    }
    
    [self reloadPictureData];
}

-(void)onClickDeleteButton:(UIButton*) button
{
    [aryPicture removeObjectAtIndex:button.tag];
    
    [self reloadPictureData];
    
    [self.delegate changedPictureView:aryPicture :self.frame.size.height];
}

-(void)reloadPictureData
{
    for (UIView *view in self.subviews)
    {
        [view removeFromSuperview];
    }
    
    int horizontalNums = 0;
    
    if (IS_IPHONE_5_OR_LESS)
        horizontalNums = 2;
    else
        horizontalNums = 3;
    
    NSInteger count = aryPicture.count;
    
    for (int i = 0; i < count; i++) {
        
        int x = (i % horizontalNums) * 90;
        int y = (i / horizontalNums) * 90;
        
        UIView *view = [[UIView alloc] initWithFrame:CGRectMake(x, y, 90, 82)];
        
        UIImage *img = [aryPicture objectAtIndex:i];
        UIImageView *imgView = [[UIImageView alloc] initWithFrame:CGRectMake(3, 3, 80, 80)];
        [imgView setImage:img];
        
        [view addSubview:imgView];
        
        UIButton *btnDel = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 17, 17)];
        btnDel.tag = i;
        [btnDel setImage:[UIImage imageNamed:@"fabu_delete"] forState:UIControlStateNormal];
        [btnDel addTarget:self action:@selector(onClickDeleteButton:) forControlEvents:UIControlEventTouchUpInside];
        
        [view addSubview:btnDel];
        
        [self addSubview:view];
    }
    
    CGRect rect = self.frame;

    NSInteger xx = (count % horizontalNums) * 90;
    NSInteger yy = (count / horizontalNums) * 90;
    
    if ( count == 6 ) {
        rect.size.width = horizontalNums * 90;
        rect.size.height = yy;
        [self setFrame:rect];

        return;
    }
    
    if(aryPicture.count < self.maximumPictureCount) {
   
        UIView *viewAdd = [[UIView alloc] initWithFrame:CGRectMake(xx, yy, 90, 82)];
        UIButton *btnPicture = [[UIButton alloc] initWithFrame:CGRectMake(2, 2, 80, 80)];
        [btnPicture addTarget:self action:@selector(onClickAddPicture:) forControlEvents:UIControlEventTouchUpInside];
        [btnPicture setBackgroundColor:BLACK_COLOR_245];
        [viewAdd addSubview:btnPicture];
    
        UIButton *btnAdd = [[UIButton alloc] initWithFrame:CGRectMake(33, 24, 19, 19)];
        [btnAdd setBackgroundImage:[UIImage imageNamed:@"fabu_add_small"] forState:UIControlStateNormal];
        [btnAdd addTarget:self action:@selector(onClickAddPicture:) forControlEvents:UIControlEventTouchUpInside];
        [viewAdd addSubview:btnAdd];
    
        UIButton *btnText = [[UIButton alloc] initWithFrame:CGRectMake(2, 48, 80, 15)];
        [btnText setTitle:strAddPic forState:UIControlStateNormal];
        [btnText.titleLabel setFont:FONT_12];
    
        [btnText setTitleColor:BLACK_COLOR_102 forState:UIControlStateNormal];
        [btnText addTarget:self action:@selector(onClickAddPicture:) forControlEvents:UIControlEventTouchUpInside];
        [viewAdd addSubview:btnText];
        [self addSubview:viewAdd];
    }
    rect.size.width = horizontalNums * 90;
    rect.size.height = yy + 90;
    [self setFrame:rect];

}

-(void)onClickAddPicture:(UIButton*) button
{
    logoPicker = [[ImageChooseViewController alloc] initWithNibName:@"ImageChooseViewController" bundle:nil];
    logoPicker.delegate = self;
    logoPicker.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    logoPicker.isSquareCrop = YES;
    [self.viewController.navigationController presentViewController:logoPicker animated:NO completion:nil];
}

#pragma mark - ImageChooseViewController
-(void)chooseViewController:(ImageChooseViewController *)vc shownImage:(UIImage *)image
{
    if(vc == logoPicker && image) {
        [aryPicture addObject:image];
        [self reloadPictureData];
        [self.delegate changedPictureView:aryPicture :self.frame.size.height];
    }else{
        [appDelegate.window makeToast:@"图片不能超过1M."
                    duration:3.0
                    position:CSToastPositionCenter
                       style:nil];
    }
}
-(void)addPicture:(UIImage*)image
{
    if(image == nil)
        return;
    [aryPicture addObject:image];
    [self reloadPictureData];
    [self.delegate changedPictureView:aryPicture :self.frame.size.height];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.

- (void)drawRect:(CGRect)rect {
    // Drawing code
}
 */


@end
