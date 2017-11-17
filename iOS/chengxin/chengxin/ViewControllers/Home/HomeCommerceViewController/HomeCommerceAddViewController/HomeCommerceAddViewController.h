//
//  HomeCommerceAddViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EditPictureView.h"

@interface HomeCommerceAddViewController : UIViewController<EditPictureViewDelegate, UITextViewDelegate>
@property (nonatomic, strong) NSString *changedProductID;
@property (weak, nonatomic) IBOutlet EditPictureView *viewPicture;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightPicView;
@property (weak, nonatomic) IBOutlet UITextField *nameTextField;
@property (weak, nonatomic) IBOutlet UITextField *priceTextField;
@property (weak, nonatomic) IBOutlet UILabel *categoryLabel;
@property (weak, nonatomic) IBOutlet UITextView *commentTextView;
@property (nonatomic, strong) IBOutlet UILabel *productCommentLimitLabel;
@property (weak, nonatomic) IBOutlet UITextField *addressTextField;
@property (weak, nonatomic) IBOutlet UITextField *weburlTextField;
@property (nonatomic, strong) IBOutlet UIView *transView;
@property (nonatomic, strong) IBOutlet UIButton *isMainTrueButton;
@property (nonatomic, strong) IBOutlet UIButton *isMainFalseButton;
@end
